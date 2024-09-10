// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import { ERC721 } from "../token/ERC721/ERC721.sol";
import { Ownable } from "../access/Ownable.sol";

contract CustomERC721 is ERC721, Ownable {
    constructor(
        string memory _name,
        string memory _symbol,
        address initialOwner
    ) ERC721(_name, _symbol) Ownable(initialOwner) {}
}
