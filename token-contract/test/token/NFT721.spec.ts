import { ethers } from "hardhat"
import { NFT721, NFT721__factory } from "../../types"
import { HardhatEthersSigner } from "@nomicfoundation/hardhat-ethers/signers"

describe.only("NFT ERC721", () => {
  let contract: NFT721
  let contractAddr = '0x5Ead635761cF5bFb93825e42d86acE2788ee8D4A'
  let owner: HardhatEthersSigner
  let addr1: HardhatEthersSigner
  let addr2: HardhatEthersSigner

  //const [owner, addr1] = await ethers.getSigners()
  beforeEach(async () => {
    [owner, addr1, addr2] = await ethers.getSigners()
  })

  it('xxx', async () => {
    console.log(`owner: ${owner.address}, addr1: ${addr1.address}, addr2: ${addr2.address}`)
  })

  it.skip("NFT ERC721 deploy contract", async () => {
    console.log(`Deploying contracts with the account: ${owner.address}`)
    const name = 'Sheep';
    const symbol = 'lee';
    const signer = owner.address;
    const contractURI = '';
    const tokenURIPrefix = '';

    const tokenFactory: NFT721__factory = (await ethers.getContractFactory("NFT721")) as NFT721__factory
    const token: NFT721 = (await tokenFactory.deploy(name, symbol, signer, contractURI, tokenURIPrefix)) as NFT721
    const tokenAddress = await token.getAddress()

    //owner address: 0x811bB0C5EB3664D7bB167Ebc1cD77322586B7f89 
    console.log(`Deploying contracts with the account: ${owner.address}, contract address: ${tokenAddress}, token owner address: ${owner.address}`)
  })
})