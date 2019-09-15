from solc import compile_files
import time

from cpc_fusion import Web3


def init():
    cf = Web3(Web3.HTTPProvider("http://127.0.0.1:8521"))
    owner = cf.toChecksumAddress("b3801b8743dea10c30b0c21cae8b1923d9625f84")

    return cf, owner


def compile_file():
    output = compile_files(["../crl.sol"])
    abi = output['../crl.sol:CRL']["abi"]
    bin = output['../crl.sol:CRL']["bin"]
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
    crl = cf.cpc.contract(abi=config["abi"], address=contract_address)
    return contract_address, crl


def test_case():
    print("[1. compile and deploy contract]")
    cf, owner = init()
    config = compile_file()
    contract_address, crl = deploy_contract(config, cf, owner)
    print("contract address: ", contract_address)

    print("[2. check validity]")
    vc_id = "1234"
    result = crl.functions.is_valid(vc_id).call()
    print("result: ", result)

    print("[3. revoke cred]")
    tx_hash = crl.functions.revoke(vc_id).transact({"from": owner, "value": 0})
    tx_receipt = cf.cpc.waitForTransactionReceipt(tx_hash)
    print("revoke result: ", tx_receipt["status"])

    print("[4. check validity again]")
    vc_id = "1234"
    result = crl.functions.is_valid(vc_id).call()
    print("result: ", result)


def main():
    test_case()


if __name__ == '__main__':
    main()
