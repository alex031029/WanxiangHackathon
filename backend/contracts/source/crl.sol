pragma solidity ^0.4.24;

import "./lib/StringUtils.sol";
pragma experimental ABIEncoderV2;

contract CRL {
    using StringUtils for string;

    string[] public crl;

    function revoke(string vc_id) public {
        crl.push(vc_id);
    }

    function is_valid(string vc_id) public view returns (bool) {
//        for(uint i = 0; i < crl.length; i++) {
//            if(StringUtils.equal(vc_id, crl[i])) {
//                return false;
//            }
//        }
        return true;
    }
}
