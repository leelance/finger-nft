import { ethers } from "hardhat"
import { NFT721, NFT721__factory } from "../../types"
import { HardhatEthersSigner } from "@nomicfoundation/hardhat-ethers/signers"

describe.only("NFT ERC721", () => {
  let contract: NFT721
  let contractAddr = '0xA5962D82781Ef9B0fFa266B740b75A123DE27ACd'
  let owner: HardhatEthersSigner
  let addr1: HardhatEthersSigner
  let addr2: HardhatEthersSigner

  beforeEach(async () => {
    [owner, addr1, addr2] = await ethers.getSigners()
  })

  it('init hardhat default address', async () => {
    console.log(`owner: ${owner.address}, addr1: ${addr1.address}, addr2: ${addr2.address}`)
  })

  it("NFT ERC721 deploy contract", async () => {
    console.log(`Deploying contracts with the account: ${owner.address}`)
    const name = 'Sheep1';
    const symbol = 'lee';
    const signer = addr1.address;
    const contractURI = '';
    const tokenURIPrefix = '';

    const tokenFactory: NFT721__factory = (await ethers.getContractFactory("NFT721")) as NFT721__factory
    const token: NFT721 = (await tokenFactory.deploy(name, symbol, signer, contractURI, tokenURIPrefix)) as NFT721
    const tokenAddress = await token.getAddress()

    //owner address: 0xA5962D82781Ef9B0fFa266B740b75A123DE27ACd 
    console.log(`Deploying contracts with the account: ${owner.address}, contract address: ${tokenAddress}, token owner address: ${owner.address}`)
  })
})