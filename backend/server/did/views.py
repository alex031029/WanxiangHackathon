from django.http import HttpResponse, JsonResponse
from django.shortcuts import render
from django.db.models import Q
from did.models import Issuer
from django.core import serializers


# Create your views here.

def search(req):
    name = req.GET.get('query_key')
    issuers = Issuer.objects.filter(Q(name__contains=name) | Q(industry__contains=name))
    print(issuers)
    data = serializers.serialize('json', issuers)
    return HttpResponse(data)


def detail(req):
    m = {"k": "v"}
    return JsonResponse(m)


def apply(req):
    return JsonResponse({})
