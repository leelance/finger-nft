// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import { ERC20 } from "../token/ERC20/ERC20.sol";
import { ERC20Burnable } from "../token/ERC20/extensions/ERC20Burnable.sol";

contract CustomERC20Burnable is ERC20Burnable {
    constructor(string memory _name, string memory _symbol, uint256 _supply) ERC20(_name, _symbol) {
        _mint(msg.sender, _supply * 10 ** 18);
    }
}
