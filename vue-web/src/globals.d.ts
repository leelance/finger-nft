import { MetaMaskInpageProvider } from "@metamask/providers"
import { Web3, RegisteredSubscription } from 'web3'

declare global {
  interface Window {
    ethereum?: MetaMaskInpageProvider,
    wallet?: Web3<RegisteredSubscription>,
  }
}