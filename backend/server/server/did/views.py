from django.http import HttpResponse, JsonResponse
from django.shortcuts import render


# Create your views here.

def search(req):
    k = req.GET.get('did')
    # print('did is', k)
    

    return JsonResponse(k)


def detail(req):
    m = {"k": "v"}
    return JsonResponse(m)
