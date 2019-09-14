pragma solidity ^0.4.24;

import "./lib/set.sol";

contract Issuer {

    using Set for Set.Data;

    address owner;
    string name;
    string industry;

    // claims contains a list of ipfs uri of claim template files
    Set.Data private claim_templates;
    mapping(string => string) public claims;

    // crl is credential revocation list
    Set.Data private crl;

    modifier onlyOwner() {require(msg.sender == owner);_;}

    constructor(address issuer, string _name, string _industry) public {
        owner = issuer;
        name = _name;
        industry = _industry;
    }

    function update_info(string _name, string _industry) public onlyOwner {
        name = _name;
        industry = _industry;
    }

    function publish_claim(string type, string uri) public onlyOwner {
        require(!claim_templates.contains(type));
        claims[type] = uri;
        claim_templates.insert(type);
    }

    function remove_claim(string type) public onlyOwner {
        require(claim_templates.contains(type));
        claim_templates.remove(type);
    }

    function revoke(string vc_id) public onlyOwner {
        crl.insert(vc_id);
    }

    function is_validate(string vc_id) public view returns (bool) {
        return claim_templates.contains(vc_id);
    }
}