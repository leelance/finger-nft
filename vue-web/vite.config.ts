import { join, } from 'path'
import Vue from '@vitejs/plugin-vue'
import { defineConfig, loadEnv } from 'vite';
import type { ConfigEnv } from 'vite'

import ViteComponents from 'vite-plugin-components'
import { viteMockServe } from 'vite-plugin-mock'
import viteCompression from 'vite-plugin-compression'
import { nodePolyfills } from 'vite-plugin-node-polyfills'


export default defineConfig(({ command, mode }: ConfigEnv) => {
  const resolve = (dir: string) => join(__dirname, dir)
  const env = loadEnv(mode, process.cwd(), '');

  console.log(`===>api url: ${env.VITE_VUE_APP_API_URL}`)
  return {
    resolve: {
      extensions: ['.js', '.jsx', '.ts', '.tsx', '.json', '.vue'],
      alias: {
        '@': resolve('src'),
      },
    },
    build: {
      target: 'es2015',
    },
    css: {
      preprocessorOptions: {
        scss: {
          additionalData: `@import "@/styles/variables.scss";`
        }
      }
    },
    plugins: [
      Vue(),
      nodePolyfills(),
      ViteComponents(),
      viteCompression(),
      viteMockServe({
        mockPath: 'src/mock',
        watchFiles: true,
      }),
    ],
    server: {
      host: '0.0.0.0',
      proxy: {
        "/fingernft": {
          target: env.VITE_VUE_APP_API_URL,
          changeOrigin: true,
        },
        "/oauth": {
          target: env.VITE_VUE_APP_OAUTH_URL,
          changeOrigin: true,
          rewrite: path => path.replace(/^\/oauth/, ''),
        },
      },
    },
  }
})
