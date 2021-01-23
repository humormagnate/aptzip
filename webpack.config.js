const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const OptimizeCssAssetsPlugin = require("optimize-css-assets-webpack-plugin");

// enable in development only
const devMode = process.env.NODE_ENV !== "production";
const plugins = [];
if (devMode) {
  plugins.push(
    new MiniCssExtractPlugin({
      filename: "[name].min.css",
      chunkFilename: "[id].min.css",
      ignoreOrder: false, // Enable to remove warnings about conflicting order
    })
  );
}

module.exports = {
  entry: [
    "./src/main/resources/static/js/aptzip/main.js",
    "./src/main/resources/static/scss/forum.scss",
    "./src/main/resources/static/scss/aptzip.scss",
  ],
  target: "node",
  output: {
    path: __dirname + "/src/main/resources/static/dist",
    filename: "[name].min.js",
  },
  mode: "development",
  plugins,
  module: {
    rules: [
      {
        test: /\.(eot|svg|ttf|woff|woff2)$/i,
        use: {
          loader: "url-loader",
          options: {
            name: "[name].[ext]",
            encoding: false,
          },
        },
      },
      {
        test: /\.js$/i,
        exclude: /(node_modules|bower_components)/,
        use: {
          loader: "babel-loader",
          options: {
            presets: ["@babel/preset-env"], // babel 7
            // presets: ['@babel/preset-es2015'] // old babel
            plugins: [
              [
                "@babel/plugin-transform-runtime",
                {
                  regenerator: true,
                },
              ],
            ],
          },
        },
      },
      {
        test: /\.(s[ac]ss|css)$/i,
        use: [
          {
            loader: MiniCssExtractPlugin.loader,
            options: {
              publicPath: "/src/main/resources/static/",
            },
          },
          {
            // Translates CSS into CommonJS
            loader: "css-loader",
            options: {
              sourceMap: true,
            },
          },
          {
            // Compiles Sass to CSS
            loader: "sass-loader",
            options: {
              sourceMap: true,
              sassOptions: {
                minimize: true,
                outputStyle: "compressed",
                indentWidth: 2,
              },
            },
          },
        ],
      },
    ],
  },
  optimization: {
    minimizer: [
      new OptimizeCssAssetsPlugin({
        cssProcessorOptions: {
          map: {
            inline: false,
            annotation: true,
          },
        },
      }),
    ],
  },
};
