import { ethers } from "hardhat"
import type { CustomERC721__factory } from "../../types/factories/custom/CustomERC721__factory"
import { CustomERC721 } from "../../types"

describe("Custom ERC721", function () {
    let contract: CustomERC721;
    beforeEach(async () => {
        const factory = await ethers.getContractFactory("CustomERC721")
        contract = factory.attach('0xd5E62AfF1Fc601c604057704268c582807C44345') as CustomERC721
    });

    it.skip("ERC721 deploy contract", async () => {
        const [owner, addr1] = await ethers.getSigners()
        console.log(`Deploying contracts with the account: ${owner.address}`)

        const tokenName = "Hello Pin"
        const symbol = "PIN"

        const tokenFactory: CustomERC721__factory = (await ethers.getContractFactory("CustomERC721")) as CustomERC721__factory
        const token: CustomERC721 = (await tokenFactory.deploy(tokenName, symbol, owner)) as CustomERC721
        const tokenAddress = await token.getAddress()

        //owner address: 0x811bB0C5EB3664D7bB167Ebc1cD77322586B7f89 
        //contract: 0xd5E62AfF1Fc601c604057704268c582807C44345 
        //token owner: 0x811bB0C5EB3664D7bB167Ebc1cD77322586B7f89
        console.log(`Deploying contracts with the account: ${owner.address}, contract address: ${tokenAddress}, token owner address: ${owner.address}`)
    })

    describe('ERC721 query', () => {
        it.skip("ERC721 query token name", async () => {
            const name = await contract.name()
            console.log(`token name: ${name}`)
        })
    })

    describe('ERC721 transfer owner ship', () => {
        it.skip("ERC721 query owner", async () => {
            const owner = await contract.owner()
            console.log(`contract owner: ${owner}`)
        })

        it.skip("ERC721 renounce owner ship", async () => {
            const [owner, addr1] = await ethers.getSigners()
            console.log(`contract renounce owner: ${addr1.address}`)

            await contract.connect(addr1).renounceOwnership()
        })

        it.skip("ERC721 transfer owner ship", async () => {
            const [owner, addr1] = await ethers.getSigners()
            console.log(`contract transfer owner: ${addr1.address}`)

            await contract.transferOwnership(addr1)
        })
    })
})