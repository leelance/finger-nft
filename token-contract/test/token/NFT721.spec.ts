import { ethers } from "hardhat"
import { NFT721, NFT721__factory } from "../../types"
import { HardhatEthersSigner } from "@nomicfoundation/hardhat-ethers/signers"

describe.only("NFT ERC721", () => {
  let owner: HardhatEthersSigner
  let addr1: HardhatEthersSigner
  let addr2: HardhatEthersSigner

  beforeEach(async () => {
    [owner, addr1, addr2] = await ethers.getSigners()
  })

  it('init hardhat default address', async () => {
    console.log(`owner: ${owner.address}, addr1: ${addr1.address}, addr2: ${addr2.address}`)
  })

  it.skip("NFT ERC721 deploy contract", async () => {
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

  describe("NFT ERC721 mint", () => {
    let contract: NFT721
    let contractAddr = '0x5f7FDE74b2ec78683F31E47492c752bfcE87A0aB'

    beforeEach(async () => {
      const factory = await ethers.getContractFactory("NFT721")
      contract = factory.attach(contractAddr) as NFT721
    })

    it.skip('NFT mint', async () => {
      const name = await contract.name()
      const symbol = await contract.symbol()
      console.log(`===>name: ${name}, symbol: ${symbol}`)

      //uint256 tokenId, uint8 v, bytes32 r, bytes32 s, Fee[] memory _fees, string memory tokenURI
      const tokenId = 9
      const r = "0x3e2051d107c7a2bf4cb4150b34bfc114b1a66bbcc7b8a57e53bdd14d9a26128f"
      const s = "0x2c75f9d024bf2b7bb8243c89df6bca041069c967e9971ba4ce341d6ee01597af"
      const v = "28"
      const _fees: any = []
      const tokenURI = "/ipfs/QmVCKGQcYJgj2BaACMU7z6Fi5hYj3hDXmRGGcdJQ8oPz6F"
      _fees.push({ "recipient": "0x811bb0c5eb3664d7bb167ebc1cd77322586b7f89", "value": 200 })

      await contract.mint(tokenId, v, r, s, _fees, tokenURI)
    })

    it('NFT address by tokenId', async () => {
      const tokenId = 9
      const tokenIdAddress = await contract.tokenURI(tokenId)

      console.log(`===>TokenIdAddress: ${tokenIdAddress}`)
    })

    it('NFT mint estimate gas', async () => {
      const tokenId = 9
      const r = "0x3e2051d107c7a2bf4cb4150b34bfc114b1a66bbcc7b8a57e53bdd14d9a26128f"
      const s = "0x2c75f9d024bf2b7bb8243c89df6bca041069c967e9971ba4ce341d6ee01597af"
      const v = "28"
      const _fees: any = []
      const tokenURI = "/ipfs/QmVCKGQcYJgj2BaACMU7z6Fi5hYj3hDXmRGGcdJQ8oPz6F"
      _fees.push({ "recipient": "0x811bb0c5eb3664d7bb167ebc1cd77322586b7f89", "value": 200 })

      //const beforeTokenIdAddress = await contract.tokenURI(tokenId)
      ///await contract.mint(tokenId, v, r, s, _fees, tokenURI)
    })
  })

})