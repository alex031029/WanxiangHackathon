import json
from datetime import datetime, timedelta

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


#
# d = {
#     "issuer_did": "did:eth:d6DaE32b2F55fBadeAEb23819d6c3b6083eFbE0d",
#     "applicant_did": "did:eth:d6DaE32b2F55fBadeAEb23819d6c3b6083eFbE0d",
#     "type": "charging",
#     "color": "red",
#     "brand": "karma",
#     "battery_capacity": "8888",
#     "sig": "0xf036e92dba5722e76bff2b359f3705e63e7f77694c58c829b91c7973547a45922f3f1bd7816ba750dec074a0945fbebe1d1aab1fac8084557e984ab08505235701",
#     "id": "2c26caddcbd6d3c8a5a002b3533cfb50",
#     "create_time": "2019-09-14T09:05:48.724",
#     "expired_time": "2020-09-13T09:05:48.724"
# }


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


def make_sig(msg):
    with open(f'assets/issuer_key') as keyfile:
        encrypted_key = keyfile.read()
        private_key = w3.eth.account.decrypt(encrypted_key, 'Abc123456')
        pk = keys.PrivateKey(private_key)
        msg = msg.encode()
        signature = pk.sign_msg(msg)
        # print(pk)
        # 0x8f704eb3074be5b70b84974536a130cf2a3f82f2775a4ffbcbd6a3afd7ed46fc

        # print(pk.public_key)
        # 0x37411435cd7c35366c84625d44779b5363e4ac33f7dc01748715e1e4dd76d2ea60c96a98c391d059607fb4ed2797468dd2ee2482e8f9a46b16fbd94bdfb92df2

        # print(signature)
        # 0xc91c9f8774574155bdcb845fea373eac2e83274f871d4b0223d6b2806ad40e17255a5e7b6b3f72f3572c6b3d5a79e8ed32fa307c5fd94098b789cc88d684952c00

        print(pk.public_key.to_checksum_address())
        print(111, signature.recover_public_key_from_msg(msg))
        print(msg)
        print(pk.public_key)
        print(222, signature.verify_msg(msg, pk.public_key))
        return signature


from eth_keys.datatypes import (
    LazyBackend,
    PublicKey,
    PrivateKey,
    Signature, )


def verify(req):
    vc = req.GET.get('vc')
    vc = json.loads(vc)
    print(vc)
    sig_from_client = vc.get('sig')
    issuer_did = vc.get('issuer_did')
    applicant_did = vc.get('applicant_did')
    type = vc.get('type')
    color = vc.get('color')
    brand = vc.get('brand')
    battery_capacity = vc.get('battery_capacity')
    msg = {"issuer_did": issuer_did, 'applicant_did': applicant_did, "type": type, "color": color, "brand": brand,
           "battery_capacity": battery_capacity}
    print('sig_from_client', sig_from_client)

    with open(f'assets/issuer_key') as keyfile:
        encrypted_key = keyfile.read()
        private_key = w3.eth.account.decrypt(encrypted_key, 'Abc123456')
        pk = keys.PrivateKey(private_key)
        msg = json.dumps(msg).encode()
        signature = pk.sign_msg(msg)

    return JsonResponse({"result": signature.verify_msg(msg, pk.public_key)})
