pragma solidity ^0.4.24;

library StringSet {
    /** We define a new struct datatype that will be used to
     * hold its data in the calling contract.
    */
    struct Data {
        mapping(string => bool) flags;
        mapping(string => uint) pos;
        string[] values;
    }

    /** Note that the first parameter is of type "storage
     * reference" and thus only its storage address and not
     * its contents is passed as part of the call.  This is a
     * special feature of library functions.  It is idiomatic
     * to call the first parameter 'self', if the function can
     * be seen as a method of that object.
    */
    function insert(Data storage self, string value)
    internal
    returns (bool)
    {
        if (self.flags[value])
            return false; // already there
        self.flags[value] = true;
        self.pos[value] = self.values.length;
        self.values.push(value);
        return true;
    }

    function remove(Data storage self, string value)
    internal
    returns (bool)
    {
        if (!self.flags[value]) {
            return false; // not there
        }
        self.flags[value] = false;
        uint size = self.values.length;
        uint position = self.pos[value];
        string last = self.values[size-1];
        self.values[position] = last;
        self.pos[last] = position;
        delete self.values[size-1];
        self.values.length--;

        return true;
    }

    function contains(Data storage self, string value) internal view returns (bool) {
        return self.flags[value];
    }

    function getAll(Data storage self) internal view returns (string[]) {
        return self.values;
    }
}
