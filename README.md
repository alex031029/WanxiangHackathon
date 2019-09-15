# Wanxiang Blockchain Hackathon
 
This project is for Wanxiang Blockchain Hackathon 2019. We implement a **VID** (Vehicle Identity) system on Ethereum blockchain.

Team 4: **Shi Yanchen, Liu Shaowei, Wu Jiajing, Kang Beibei**

# Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Quick Start](#quick-start)

---
## Overview

VID is a fexible digital identity for each entities involved in automotive industry as well related services. It can also solve the trust issue in V2X, inspired by PKI (public key infrastructure). 

---
## Features

There are three main roles in VID ecosystem: **issuer**, **holder** and **verifier**. Note that these roles are not exclusive, i.e, an entity can fit in multiple roles at the same time.

### Issuer

An issuer is an entity to issue the credential for each applicant. If an issuer receives a claim from an applicant, it can grant a **verfiable credential** to this applicant (which is now a holder). 

All this information (VC and holder's address) are recorded in blockchain for later verification.

### Holder

A holder is an entity hold one or multiple VCs. There are two functions a holder can invoke:

* Claim: sent an application to issuer for VC.
* Request: sent a request along with its VID to verifier. After verification, it can collect corresponding service.

### Verifier

A verifier is for verifying each request from a holder based on its VID and embedded VCs. Each verifier recognizes a set of VCs, verifies the holder if the holder has a recognizable VC.

This verfication can be either 

* pairwise, only the holder and verifier are involved in this process, or
* assisted by blockchain, the verifier ask for the chain to check latest VC information.



---
## Quick Start

Our demonstration is a IOS and Android application. 

* Compile and deploy smart contracts
* Launch a backend server
* Download binary releases and install in your phone
* Run and enjoy it :)

