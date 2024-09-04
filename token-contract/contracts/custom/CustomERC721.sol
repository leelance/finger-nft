// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import { ERC721 } from "../token/ERC721/ERC721.sol";

contract CustomERC721 is ERC721 {
    constructor(string memory _name, string memory _symbol) ERC721(_name, _symbol) {}
}
