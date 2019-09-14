from django.http import HttpResponse, JsonResponse
from django.shortcuts import render


# Create your views here.

def search(req):
    name = req.GET.get('name')

    return JsonResponse({})


def detail(req):
    m = {"k": "v"}
    return JsonResponse(m)


def apply(req):
    return JsonResponse({})
