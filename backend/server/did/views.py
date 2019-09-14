from django.http import HttpResponse, JsonResponse
from django.shortcuts import render
from django.db.models import Q
from did.models import Issuer, Credential
from django.core import serializers


# search issuer with query_key
def search(req):
    name = req.GET.get('query_issuer')
    issuers = Issuer.objects.filter(Q(name__contains=name) | Q(industry__contains=name))
    print(issuers)
    data = serializers.serialize('json', issuers)
    return HttpResponse(data)


def detail(req):
    if req.method == 'GET':
        return HttpResponse("applicant_did type color brand battery_capacity")
    else:
        return HttpResponse("requset invlid")


def apply(req):
    if req.method == "POST":
        applicant_did = req.POST.get('applicant_did')
        type = req.POST.get('type')
        color = req.POST.get('color')
        brand = req.POST.get('brand')
        battery_capacity = req.POST.get('battery_capacity')
        try:
            return HttpResponse('post ok')
        except Exception as e:
            return HttpResponse(e)
        # detail.info
        # proof.
        return JsonResponse({})
    else:
        return HttpResponse("requset invlid")
