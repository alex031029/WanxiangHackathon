pragma solidity ^0.4.24;

import "./lib/StringUtils.sol";

pragma experimental ABIEncoderV2;

contract Issuer {

    using StringUtils for string;
    address owner;
    string name;
    string industry;

    // claims contains a list of ipfs uri of claim template files
    struct ClaimTemplate {
        string cred_type;
        string uri;
    }
    ClaimTemplate[] public claims;
//    StringSet.Data private claim_templates;
//    mapping(string => string) public claims;

    // crl is credential revocation list
//    StringSet.Data private crl;
    string[] public crl;

    modifier onlyOwner() {require(msg.sender == owner);_;}

    constructor(string _name, string _industry) public {
        owner = msg.sender;
        name = _name;
        industry = _industry;
    }

    function update_info(string _name, string _industry) public onlyOwner {
        name = _name;
        industry = _industry;
    }

    function query_claims(string _type) public view returns (string) {
        for(uint i = 0; i < claims.length; i++) {
            if(StringUtils.equal(_type, claims[i].cred_type)) {
                return claims[i].uri;
            }
        }
        string memory no_result = "there is no such claim type";
        return no_result;
    }

    function publish_claim(string _type, string uri) public onlyOwner {
//        require(!claim_templates.contains(_type));
        ClaimTemplate memory new_claim = ClaimTemplate(_type, uri);
        claims.push(new_claim);
    }

    function revoke(string vc_id) public onlyOwner {
        crl.push(vc_id);
    }

    function is_valid(string vc_id) public view returns (bool) {
        for(uint i = 0; i < crl.length; i++) {
            if(StringUtils.equal(vc_id, crl[i])) {
                return false;
            }
        }
        return true;
    }
}