from django.db import models


# Create your models here.

class Issuer(models.Model):
    did = models.CharField(max_length=48)
    name = models.CharField(max_length=100)
    industry = models.CharField(max_length=100)

    def __str__(self):
        return self.did


class Claim(models.Model):
    id = models.IntegerField(primary_key=True)
    applicant_did = models.CharField(max_length=48)
    # charging
    type = models.CharField(max_length=100)
    color = models.CharField(max_length=200)
    brand = models.CharField(max_length=200)
    battery_capacity = models.CharField(max_length=100)
#
# class Proof(models.Model):
#     '''
#     did:eth:d6DaE32b2F55fBadeAEb23819d6c3b6083eFbE0d
#     lowercase
#     '''
#     id = weiyi
#     issuer_did= models.CharField(max_length=200)
#     sig= models.BinaryField(max_length=200)
#
#
# class Credential(models.Model):
#     id = models 唯一
#     create_time =
#     expired_time =
#     claim =  claim_id foreinkey
#     proof =  proof_id forekey
#
