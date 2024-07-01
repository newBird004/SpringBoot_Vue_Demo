// https://eslint.org/docs/user-guide/configuring

module.exports = {
  root: true,
  parser: 'vue-eslint-parser',
  parserOptions: {
    parser: 'babel-eslint',
    sourceType: 'module',
  },
  env: {
    browser: true,
    node: true,
    es6: true,
  },
  extends: 'eslint:recommended',
  // required to lint *.vue files
  plugins: ['vue'],
  globals: {
    document: true,
    window: true,
  },
  // add your custom rules here
  rules: {
    //0--不启用； 1--出现问题会有警告； 2--出现问题会报错
    indent: [
      'off',
      2,
      {
        SwitchCase: 1, // （默认：0）指定 switch-case 语句的缩进级别
      },
    ], // 强制使用一致的缩进
    // eqeqeq: [2, 'always'], // 要求使用 === 和 !==
    eqeqeq: ['off'], // 关闭要求使用 === he !==
    quotes: [2, 'single'], // 强制使用一致的反勾号、双引号或单引号
    'no-console': 'off',
    camelcase: 2, //强制驼峰法命名
    // allow debugger during development
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
  },
}