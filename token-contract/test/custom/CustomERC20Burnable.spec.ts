import { ethers } from "hardhat"
import type { CustomERC20Burnable__factory } from "../../types/factories/custom/CustomERC20Burnable__factory"
import { CustomERC20Burnable } from "../../types"
import { loadFixture } from "@nomicfoundation/hardhat-network-helpers"

describe("Custom ERC20 burnable", () => {
    async function deployTokenFixture() {
        const [owner, addr1, addr2] = await ethers.getSigners();
        const tokenName = "Hello Pin"
        const symbol = "PIN"
        const supply = 10000

        const tokenFactory: CustomERC20Burnable__factory = (await ethers.getContractFactory("Token")) as CustomERC20Burnable__factory
        const token: CustomERC20Burnable = (await tokenFactory.deploy(tokenName, symbol, supply)) as CustomERC20Burnable
        const tokenAddress = await token.getAddress()

        return { tokenName, symbol, supply, token, owner, tokenAddress, addr1, addr2 };
    }

     // ERC20Burnable
     it.skip("ERC20 burn token", async () => {
        const { token, owner, addr1, addr2 } = await loadFixture(deployTokenFixture);
        console.log(`owner address: ${owner.address}, addr1: ${addr1.address}, add2: ${addr2.address}`)

        const beforeBalance = await token.balanceOf(owner.address)
        await token.burn(10)
        const afterBalance = await token.balanceOf(owner.address)

        console.log(`token owner before balance: ${beforeBalance}, after: ${afterBalance}`)
    })

    it.skip("ERC20 burnFrom token", async () => {
        const { token, owner, addr1, addr2 } = await loadFixture(deployTokenFixture);
        console.log(`owner address: ${owner.address}, addr1: ${addr1.address}, add2: ${addr2.address}`)

        await token.approve(addr1.address, 100)
        const beforeBalance = await token.allowance(owner.address, addr1.address)
        await token.connect(addr1).burnFrom(owner.address, 20)
        const afterBalance = await token.allowance(owner.address, addr1.address)

        console.log(`token owner before balance: ${beforeBalance}, after: ${afterBalance}`)
    })
})