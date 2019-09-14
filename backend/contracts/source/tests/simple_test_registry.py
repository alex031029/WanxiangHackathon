from solc import compile_files
import time

from cpc_fusion import Web3


def init():
    cf = Web3(Web3.HTTPProvider("http://127.0.0.1:8521"))
    owner = cf.toChecksumAddress("b3801b8743dea10c30b0c21cae8b1923d9625f84")

    return cf, owner


def compile_file():
    output = compile_files(["../issuer_registry.sol"])
    abi = output['../issuer_registry.sol:Registry']["abi"]
    bin = output['../issuer_registry.sol:Registry']["bin"]
    print(abi)
    print(bin)
    config = dict(abi=abi, bin=bin)

    return config


def deploy_contract(config, cf, owner):
    contract = cf.cpc.contract(abi=config["abi"], bytecode=config["bin"])
    estimated_gas = contract.constructor().estimateGas()
    cf.personal.unlockAccount(owner, "password")
    tx_hash = contract.constructor().transact({"from": owner, "gas": estimated_gas})
    tx_receipt = cf.cpc.waitForTransactionReceipt(tx_hash)
    print("deploy contract, result: ", tx_receipt["status"])
    contract_address = tx_receipt["contractAddress"]
    registry = cf.cpc.contract(abi=config["abi"], address=contract_address)
    return contract_address, registry


def generate_nodes(num, cf, owner, value):
    enodes = []
    for i in range(num):
        enode = cf.toChecksumAddress(cf.personal.newAccount("password"))
        enodes.append(enode)
        cf.cpc.sendTransaction({"from": owner, "to": enode, "value": cf.toWei(value, "ether")})
    print("wait for tx confirmation...")
    time.sleep(21)
    for enode in enodes:
        balance = cf.fromWei(cf.cpc.getBalance(enode), "ether")
        print("address: ", enode)
        print("balance: ", balance)
    return enodes


def simple_test_case():
    print("[1. compile and deploy contract]")
    cf, owner = init()
    config = compile_file()
    contract_address, registry = deploy_contract(config, cf, owner)
    print("contract address: ", contract_address)

    print("[2. register as an issuer]")
    print("current node address: ", owner)
    tx_hash = registry.functions.register(cf.toChecksumAddress("0x2A186bE66Dd20c1699Add34A49A3019a93a7Fcd0")).transact({"from": owner, "value": 0})
    tx_receipt = cf.cpc.waitForTransactionReceipt(tx_hash)
    print("register result: ", tx_receipt["status"])

    print("[3. check issuer status]")
    is_issuer = registry.functions.is_issuer(owner).call()
    print("is issuer: ", is_issuer)
    contract_addr = registry.functions.issuer_contracts(owner).call()
    print("issuer contract address: ", contract_addr)

    print("[4. sign out registry]")
    tx_hash = registry.functions.sign_out().transact({"from": owner, "value": 0})
    tx_receipt = cf.cpc.waitForTransactionReceipt(tx_hash)
    print("sign out result: ", tx_receipt["status"])

    print("[5. check issuer status again]")
    is_issuer = registry.functions.is_issuer(owner).call()
    print("is issuer: ", is_issuer)


def main():
    simple_test_case()


if __name__ == '__main__':
    main()