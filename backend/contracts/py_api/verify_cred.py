from solc import compile_files
from cpc_fusion import Web3


def init():
    cf = Web3(Web3.HTTPProvider("http://127.0.0.1:8521"))
    owner = cf.toChecksumAddress("b3801b8743dea10c30b0c21cae8b1923d9625f84")

    return cf, owner


def compile_file():
    output = compile_files(["../source/crl.sol"])
    abi = output['../source/crl.sol:CRL']["abi"]
    bin = output['../source/crl.sol:CRL']["bin"]
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


def check_cred(vc_id, issuer_did):
    # TODO: interact with chain
    return True