import json
from datetime import datetime, timedelta
from eth_account import Account

from eth_account.messages import defunct_hash_message
from django.core import serializers
from django.db.models import Q
from django.http import HttpResponse, JsonResponse
from eth_keys import keys, KeyAPI
from web3.auto import w3

from did.models import Credential, Issuer


# search issuer with query_key
def search(req):
    name = req.GET.get('query_issuer')
    issuers = Issuer.objects.filter(Q(name__contains=name) | Q(industry__contains=name))
    data = serializers.serialize('json', issuers)
    return HttpResponse(data)


def detail(req):
    if req.method == 'GET':
        return HttpResponse("applicant_did type color brand battery_capacity")
    else:
        return HttpResponse("invalid requset ")


# >>> from eth_keys import keys
# >>> pk = keys.PrivateKey(b'\x01' * 32)
# >>> signature = pk.sign_msg(b'a message')
# >>> pk
# '0x0101010101010101010101010101010101010101010101010101010101010101'
# >>> pk.public_key
# '0x1b84c5567b126440995d3ed5aaba0565d71e1834604819ff9c17f5e9d5dd078f70beaf8f588b541507fed6a642c5ab42dfdf8120a7f639de5122d47a69a8e8d1'
# >>> signature
# '0xccda990dba7864b79dc49158fea269338a1cf5747bc4c4bf1b96823e31a0997e7d1e65c06c5bf128b7109e1b4b9ba8d1305dc33f32f624695b2fa8e02c12c1e000'
# >>> pk.public_key.to_checksum_address()
# '0x1a642f0E3c3aF545E7AcBD38b07251B3990914F1'
# >>> signature.verify_msg(b'a message', pk.public_key)
# True
# >>> signature.recover_public_key_from_msg(b'a message') == pk.public_key
# True
def apply(req):
    if req.method == "POST":
        issuer_did = req.POST.get('issuer_did')
        applicant_did = req.POST.get('applicant_did')
        type = req.POST.get('type')
        color = req.POST.get('color')
        brand = req.POST.get('brand')
        battery_capacity = req.POST.get('battery_capacity')
        # proof.
        msg = {"issuer_did": issuer_did, 'applicant_did': applicant_did, "type": type, "color": color, "brand": brand,
               "battery_capacity": battery_capacity}
        sig = str(make_sig(json.dumps(msg)))
        create_time = datetime.now()
        expired_time = create_time + timedelta(days=365)

        # cal id
        import hashlib
        m = hashlib.md5()
        m.update(json.dumps(msg).encode())
        id = m.hexdigest()
        credential = Credential(id=id, applicant_did=applicant_did, type=type, color=color, brand=brand,
                                battery_capacity=battery_capacity, issuer_did=issuer_did, sig=sig,
                                create_time=create_time, expired_time=expired_time)
        # credential.save()
        credential_dict = msg
        credential_dict['sig'] = sig
        credential_dict['id'] = id
        credential_dict['create_time'] = create_time
        credential_dict['expired_time'] = expired_time

        return JsonResponse(credential_dict)
    else:
        return HttpResponse("invalid requset ")


def make_sig(msg):
    with open(f'assets/issuer_key') as keyfile:
        encrypted_key = keyfile.read()
        private_key = w3.eth.account.decrypt(encrypted_key, 'Abc123456')
        pk = keys.PrivateKey(private_key)
        msghash = defunct_hash_message(text=msg)
        signature = Account.signHash(message_hash=msghash, private_key=pk)
        return signature


def verify(req):
    vc_str = req.GET.get('vc')
    vc = json.loads(vc_str)
    sig_from_client = vc.get('sig')
    issuer_did = vc.get('issuer_did')
    applicant_did = vc.get('applicant_did')
    type = vc.get('type')
    color = vc.get('color')
    brand = vc.get('brand')
    battery_capacity = vc.get('battery_capacity')
    msg = {"issuer_did": issuer_did, 'applicant_did': applicant_did, "type": type, "color": color, "brand": brand,
           "battery_capacity": battery_capacity}
    msg = json.dumps(msg)
    # gen msg hash
    msghash = defunct_hash_message(text=msg)
    addr = Account.recoverHash(msghash, signature=sig_from_client)
    vc_id = vc.get('id')
    issuer_did = Credential.objects.filter(id=vc_id)[0].issuer_did
    cre_addr = issuer_did.split(":")[2].lower()
    if w3.toChecksumAddress(cre_addr) != w3.toChecksumAddress(addr):
        return JsonResponse({"status": False})

    from assets.verify_cred import check_cred
    status = check_cred(vc_id=vc_id, issuer_did=issuer_did)
    if status:
        return JsonResponse({"status": True})
    else:
        return JsonResponse({"status": False})
