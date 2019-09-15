from django.db import models


# Create your models here.

class Issuer(models.Model):
    did = models.CharField(max_length=48, primary_key=True)
    name = models.CharField(max_length=100)
    industry = models.CharField(max_length=100)

    def __str__(self):
        return self.did


class Credential(models.Model):
    id = models.CharField(max_length=100, primary_key=True)
    create_time = models.DateTimeField()
    expired_time = models.DateTimeField()
    # claim:
    applicant_did = models.CharField(max_length=48)
    type = models.CharField(max_length=100)
    color = models.CharField(max_length=200)
    brand = models.CharField(max_length=200)
    battery_capacity = models.CharField(max_length=100)
    # proof
    issuer_did = models.CharField(max_length=200)
    sig = models.CharField(max_length=200)

    def __str__(self):
        return self.id
