from django.contrib import admin

from did.models import *


# Register your models here.
class IssuerAdmin(admin.ModelAdmin):
    model = Issuer
    list_display = ['did', 'name', 'industry']


class CredentialAdmin(admin.ModelAdmin):
    model = Credential
    list_display = ['id']


admin.site.register(Issuer, IssuerAdmin)
admin.site.register(Credential, CredentialAdmin)
