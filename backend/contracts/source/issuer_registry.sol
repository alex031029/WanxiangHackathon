pragma solidity ^0.4.24;

import "./lib/set.sol";

contract issuer_registry {
    using Set for Set.Data;

    mapping(address => address) public issuer_contracts;
    Set.Data private issuers;

    function register(address contract_addr) public {
        issuer_contracts[msg.sender] = contract_addr;
        issuers.insert(msg.sender);
    }

    function sign_out() public {
        require(issuers.contains(msg.sender));
        issuers.remove(msg.sender);
    }

    function is_issuer(address _issuer) public view returns (address) {
        return issuers.contains(_issuer);
    }
}
