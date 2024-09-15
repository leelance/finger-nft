import { ethers } from "hardhat"
import { FootballPlayers, FootballPlayers__factory } from "../../types"

describe("football palyers ERC721", async () => {
    let contract: FootballPlayers
    let contractAddr = '0x5Ead635761cF5bFb93825e42d86acE2788ee8D4A'
    const [owner, addr1] = await ethers.getSigners()

    it.skip("football deploy contract", async () => {
        console.log(`Deploying contracts with the account: ${owner.address}`)

        const tokenFactory: FootballPlayers__factory = (await ethers.getContractFactory("FootballPlayers")) as FootballPlayers__factory
        const token: FootballPlayers = (await tokenFactory.deploy()) as FootballPlayers
        const tokenAddress = await token.getAddress()

        //owner address: 0x811bB0C5EB3664D7bB167Ebc1cD77322586B7f89 
        console.log(`Deploying contracts with the account: ${owner.address}, contract address: ${tokenAddress}, token owner address: ${owner.address}`)
    })

    beforeEach(async () => {
        const factory = await ethers.getContractFactory("FootballPlayers")
        contract = factory.attach(contractAddr) as FootballPlayers
    })

    describe("football palyers Deployment", async () => {
        it.skip("Should set the right owner", async () => {
            const ctOwner = await contract.owner()

            console.log(`contract token owner: ${ctOwner}, owner.address: ${owner.address}`)
        })

        it.skip("Should assign the contract name and symbol correctly", async () => {
            const tokenName = await contract.name()
            const tokenSymbol = await contract.symbol()

            console.log(`contract token name: ${tokenName}, token symbol: ${tokenSymbol}`)
        })
    })

    describe("football palyers Minting", async () => {
        it.skip("Should mint a new token and assign it to owner", async () => {
            const tokenId = 10000
            // 10000, 20000
            await contract.safeMint(owner.address, tokenId)
            const tokenOwner = await contract.ownerOf(tokenId)

            console.log(`contract token ownerOf: ${tokenOwner}`)
        })

        it("Should fail if non-owner tries to mint", async () => {
        
            const tokenId = 10000
            const tx = await contract.connect(addr1).safeMint(addr1.address, tokenId)

            const result = await tx.wait()
            console.log(result)
        })
    })
})