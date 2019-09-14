from solc import compile_files
import time

from cpc_fusion import Web3


def init():
    cf = Web3(Web3.HTTPProvider("http://127.0.0.1:8521"))
    owner = cf.toChecksumAddress("b3801b8743dea10c30b0c21cae8b1923d9625f84")

    return cf, owner


def compile_file():
    output = compile_files(["../issuer.sol"])
    abi = output['../issuer.sol:Issuer']["abi"]
    bin = output['../issuer.sol:Issuer']["bin"]
    print(abi)
    print(bin)
    config = dict(abi=abi, bin=bin)

    return config


def deploy_contract(config, cf, owner):
    contract = cf.cpc.contract(abi=config["abi"], bytecode=config["bin"])
    estimated_gas = contract.constructor().estimateGas()
    cf.personal.unlockAccount(owner, "password")
    tx_hash = contract.constructor("karma", "charging").transact({"from": owner, "gas": estimated_gas})
    tx_receipt = cf.cpc.waitForTransactionReceipt(tx_hash)
    print("deploy contract, result: ", tx_receipt["status"])
    contract_address = tx_receipt["contractAddress"]
    issuer = cf.cpc.contract(abi=config["abi"], address=contract_address)
    return contract_address, issuer


# def generate_nodes(num, cf, owner, value):
#     enodes = []
#     for i in range(num):
#         enode = cf.toChecksumAddress(cf.personal.newAccount("password"))
#         enodes.append(enode)
#         cf.cpc.sendTransaction({"from": owner, "to": enode, "value": cf.toWei(value, "ether")})
#     print("wait for tx confirmation...")
#     time.sleep(21)
#     for enode in enodes:
#         balance = cf.fromWei(cf.cpc.getBalance(enode), "ether")
#         print("address: ", enode)
#         print("balance: ", balance)
#     return enodes


def simple_test_case():
    print("[1. compile and deploy contract]")
    cf, owner = init()
    config = compile_file()
    contract_address, issuer = deploy_contract(config, cf, owner)
    print("contract address: ", contract_address)

    print("[2. update issuer information]")
    print("before update:")
    name = issuer.functions.name().call()
    industry = issuer.functions.industry().call()
    print("issuer name: ", name)
    print("issuer industry: ", industry)
    tx_hash = issuer.functions.update_info("bwm", "parking").transact({"from": owner, "value": 0})
    tx_receipt = cf.cpc.waitForTransactionReceipt(tx_hash)
    print("update result: ", tx_receipt["status"])
    print("after update:")
    name = issuer.functions.name().call()
    industry = issuer.functions.industry().call()
    print("issuer name: ", name)
    print("issuer industry: ", industry)

    print("[3. publish a new claim]")
    tx_hash = issuer.functions.publish_claim("charging", "ipfs://xxxx").transact({"from": owner, "value": 0})
    tx_receipt = cf.cpc.waitForTransactionReceipt(tx_hash)
    print("publish result: ", tx_receipt["status"])
    print("check claim")
    uri = issuer.functions.query_claim("charging").transact({"from": owner, "value": 0})
    print("claim uri: ", uri)

    print("[4. remove a claim]")
    tx_hash = issuer.functions.remove_claim("charging").transact({"from": owner, "value": 0})
    tx_receipt = cf.cpc.waitForTransactionReceipt(tx_hash)
    print("remove claim result: ", tx_receipt["status"])
    print("check claim")
    uri = issuer.functions.query_claim("charging").transact({"from": owner, "value": 0})
    print("claim uri: ", uri)

    print("[5. check validation of credential]")
    cred_id = "abc789"
    print("credential id: ", cred_id)
    print("before revocation")
    is_valid = issuer.functions.is_valid(cred_id).call()
    print("is valid: ", is_valid)
    tx_hash = issuer.functions.revoke(cred_id).transact({"from": owner, "value": 0})
    tx_receipt = cf.cpc.waitForTransactionReceipt(tx_hash)
    print("revocation result: ", tx_receipt["status"])
    print("after revocation")
    is_valid = issuer.functions.is_valid(cred_id).call()
    print("is valid: ", is_valid)


def main():
    simple_test_case()


if __name__ == '__main__':
    main()
