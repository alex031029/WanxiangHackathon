from django.contrib import admin

from did.models import *


# Register your models here.
class IssuerAdmin(admin.ModelAdmin):
    model = Issuer
    list_display = ['did', 'name', 'industry']


class ClaimAdmin(admin.ModelAdmin):
    model = Claim
    list_display = ['id']


admin.site.register(Issuer, IssuerAdmin)
admin.site.register(Claim, ClaimAdmin)
