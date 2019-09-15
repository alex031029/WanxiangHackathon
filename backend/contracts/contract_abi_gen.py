from solc import compile_files
import json


def compile_file(contract_path, contract_name):
    output = compile_files([contract_path])
    abi = output[contract_path+":"+contract_name]["abi"]
    bin = output[contract_path+":"+contract_name]["bin"]
    config = {}
    config["abi"] = abi
    config["bin"] = bin
    print("config: ")
    print(config)

    return config


def main():
    crl_config = compile_file("./source/crl.sol", "CRL")
    with open("./assets/crl.json", "w+") as f:
        f.write(json.dumps(crl_config))

    issuer_config = compile_file("./source/issuer.sol", "Issuer")
    with open("./assets/issuer.json", "w+") as f:
        f.write(json.dumps(issuer_config))

    registry_config = compile_file("./source/issuer_registry.sol", "Registry")
    with open("./assets/registry.json", "w+") as f:
        f.write(json.dumps(registry_config))


if __name__ == '__main__':
    main()
