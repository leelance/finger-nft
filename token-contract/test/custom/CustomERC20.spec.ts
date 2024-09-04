import { ethers } from "hardhat"
import type { CustomERC20__factory } from "../../types/factories/custom/CustomERC20__factory";
import { CustomERC20 } from "../../types";
import { loadFixture } from "@nomicfoundation/hardhat-network-helpers";

describe("Custom ERC20", () => {
    async function deployTokenFixture() {
        const [owner, addr1, addr2] = await ethers.getSigners();
        const tokenName = "Hello Pin"
        const symbol = "PIN"
        const supply = 10000

        const tokenFactory: CustomERC20__factory = (await ethers.getContractFactory("Token")) as CustomERC20__factory
        const token: CustomERC20 = (await tokenFactory.deploy(tokenName, symbol, supply)) as CustomERC20
        const tokenAddress = await token.getAddress()

        return { tokenName, symbol, supply, token, owner, tokenAddress, addr1, addr2 };
    }

    it.skip("Custom ERC20 query token name", async () => {
        const { token, tokenName } = await loadFixture(deployTokenFixture);

        const name = await token.name()
        console.log(`token name: ${name}, origin name: ${tokenName}`)
    })

    it.skip("Custom ERC20 query token symbol", async () => {
        const { token, symbol } = await loadFixture(deployTokenFixture);

        const sym = await token.symbol()
        console.log(`token symbol name: ${sym}, origin symbol: ${symbol}`)
    })

    it.skip("Custom ERC20 query token supply", async () => {
        const { token, supply } = await loadFixture(deployTokenFixture);

        const totalSupply = await token.totalSupply()
        const unit = await token.decimals()
        console.log(`token supply: ${totalSupply}, unit: ${unit}, origin supply: ${supply}`)
    })

    it.skip("Custom ERC20 token decimals unit", async () => {
        const { token } = await loadFixture(deployTokenFixture);

        const unit = await token.decimals()
        console.log(`token decimal unit: ${unit}`)
    })

    it.skip("Custom ERC20 query token balanceOf", async () => {
        const { token, owner, addr1 } = await loadFixture(deployTokenFixture);

        const ownerBalance = await token.balanceOf(owner.address)
        const addr1Balance = await token.balanceOf(addr1.address)

        console.log(`token owner balance: ${ownerBalance}, addr balance: ${addr1Balance}`)
    })

    it.skip("Custom ERC20 transfer token", async () => {
        const { token, owner, addr1 } = await loadFixture(deployTokenFixture);

        const res = await token.transfer(addr1.address, 10)
        const ownerBalance = await token.balanceOf(owner.address)
        const addr1Balance = await token.balanceOf(addr1.address)

        console.log(`token owner balance: ${ownerBalance}, transfer result: ${res}, addr balance: ${addr1Balance}`)
    })

    //allowance：返回_spender还能提取token的个数
    it.skip("Custom ERC20 allowance token", async () => {
        const { token, owner, addr1 } = await loadFixture(deployTokenFixture);

        const res = await token.allowance(owner.address, addr1.address)
        const ownerBalance = await token.balanceOf(owner.address)
        const addr1Balance = await token.balanceOf(addr1.address)

        console.log(`token owner balance: ${ownerBalance}, allowance result: ${res}, addr balance: ${addr1Balance}`)
    })

    it.skip("Custom ERC20 approve token", async () => {
        const { token, owner, addr1 } = await loadFixture(deployTokenFixture);

        const beforeApp = await token.allowance(owner.address, addr1.address)
        await token.approve(addr1.address, 20)
        const afterApp = await token.allowance(owner.address, addr1.address)

        console.log(`token approve before: ${beforeApp}, after: ${afterApp}`)
    })

    it.skip("Custom ERC20 approve/transfer token", async () => {
        const { token, owner, addr1, addr2 } = await loadFixture(deployTokenFixture);
        console.log(`owner address: ${owner.address}, addr1: ${addr1.address}, add2: ${addr2.address}`)

        await token.approve(addr1.address, 20)
        const beforeApp = await token.allowance(owner.address, addr1.address)
        //连接addr1账户, 然后转账给addr2账户
        await token.connect(addr1).transferFrom(owner.address, addr2.address, 10);
        const afterApp = await token.allowance(owner.address, addr1.address)

        const ownerBalance = await token.balanceOf(owner.address)
        const addr2Balance = await token.balanceOf(addr2.address)

        console.log(`token approve before: ${beforeApp}, after: ${afterApp}, owner balance: ${ownerBalance}, addr balance: ${addr2Balance}`)
    })
})