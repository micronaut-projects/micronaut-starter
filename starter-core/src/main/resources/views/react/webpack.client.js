const path = require('path');
const webpack = require('webpack');

module.exports = {
    entry: ['./client.js'],
    devtool: false,
    output: {
        path: path.resolve(process.env.BUILD_DIR) || path.resolve(__dirname, "../../../build/js"),
        filename: 'client.js',
    },
    plugins: [
        new webpack.DefinePlugin({
            SERVER: false,
        })
    ],
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env', '@babel/preset-react']
                    }
                }
            }
        ]
    }
};
