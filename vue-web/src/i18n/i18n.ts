import { createI18n } from 'vue-i18n'
import { zh } from './langs/zh'
import { en } from './langs/en'
import enLocale from 'element-plus/es/locale/lang/en'
import zhLocale from 'element-plus/es/locale/lang/zh-cn'

const messages = {
  en: { ...en, ...enLocale },
  zh: { ...zh, ...zhLocale },
}

const i18n = createI18n({
  legacy: false,
  globalInjection: true,
  locale: localStorage.getItem('locale') || 'en',
  messages
})

//console.log(i18n.global.t('global.needLogin'))

export default i18n
