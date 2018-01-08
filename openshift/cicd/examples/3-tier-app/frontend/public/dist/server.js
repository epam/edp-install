process.env.NODE_ENV = 'development';
var path = require('path');
var chalk = require('chalk');
var webpack = require('webpack');
var WebpackDevServer = require('webpack-dev-server');
var historyApiFallback = require('connect-history-api-fallback');
var httpProxyMiddleware = require('http-proxy-middleware');
var config = require('./webpack.config');
var port = process.env.PORT || 3000;
var compiler;
var handleCompile;
var friendlySyntaxErrorLabel = 'Syntax error:';
function isLikelyASyntaxError(message) {
    return message.indexOf(friendlySyntaxErrorLabel) !== -1;
}
function formatMessage(message) {
    return message
        .replace('Module build failed: SyntaxError:', friendlySyntaxErrorLabel)
        .replace(/Module not found: Error: Cannot resolve 'file' or 'directory'/, 'Module not found:')
        .replace(/^\s*at\s.*:\d+:\d+[\s\)]*\n/gm, '')
        .replace('./~/css-loader!./~/postcss-loader!', '');
}
function clearConsole() {
    process.stdout.write('\x1bc');
}
function setupCompiler(port, protocol) {
    compiler = webpack(config, handleCompile);
    compiler.plugin('invalid', function () {
        clearConsole();
        console.log('Compiling...');
    });
    compiler.plugin('done', function (stats) {
        clearConsole();
        var hasErrors = stats.hasErrors();
        var hasWarnings = stats.hasWarnings();
        if (!hasErrors && !hasWarnings) {
            console.log(chalk.green('Compiled successfully!'));
            console.log();
            console.log('The app is running at:');
            console.log();
            console.log('  ' + chalk.cyan(protocol + '://localhost:' + port + '/'));
            console.log();
            console.log('Note that the development build is not optimized.');
            console.log('To create a production build, use ' + chalk.cyan('npm run build') + '.');
            console.log();
            return;
        }
        var json = stats.toJson({}, true);
        var formattedErrors = json.errors.map(message => 'Error in ' + formatMessage(message));
        var formattedWarnings = json.warnings.map(message => 'Warning in ' + formatMessage(message));
        if (hasErrors) {
            console.log(chalk.red('Failed to compile.'));
            console.log();
            if (formattedErrors.some(isLikelyASyntaxError)) {
                formattedErrors = formattedErrors.filter(isLikelyASyntaxError);
            }
            formattedErrors.forEach(message => {
                console.log(message);
                console.log();
            });
            return;
        }
        if (hasWarnings) {
            console.log(chalk.yellow('Compiled with warnings.'));
            console.log();
            formattedWarnings.forEach(message => {
                console.log(message);
                console.log();
            });
            console.log('You may use special comments to disable some warnings.');
            console.log('Use ' + chalk.yellow('// eslint-disable-next-line') + ' to ignore the next line.');
            console.log('Use ' + chalk.yellow('/* eslint-disable */') + ' to ignore all warnings in a file.');
        }
    });
}
function onProxyError(proxy) {
    return function (err, req, res) {
        var host = req.headers && req.headers.host;
        console.log(chalk.red('Proxy error:') + ' Could not proxy request ' + chalk.cyan(req.url) +
            ' from ' + chalk.cyan(host) + ' to ' + chalk.cyan(proxy) + '.');
        console.log('See https://nodejs.org/api/errors.html#errors_common_system_errors for more information (' +
            chalk.cyan(err.code) + ').');
        console.log();
    };
}
function addMiddleware(devServer) {
    var proxy = null;
    devServer.use(historyApiFallback({
        disableDotRule: true,
        htmlAcceptHeaders: proxy ?
            ['text/html'] :
            ['text/html', '*/*']
    }));
    if (proxy) {
        if (typeof proxy !== 'string') {
            console.log(chalk.red('When specified, "proxy" in package.json must be a string.'));
            console.log(chalk.red('Instead, the type of "proxy" was "' + typeof proxy + '".'));
            console.log(chalk.red('Either remove "proxy" from package.json, or make it a string.'));
            process.exit(1);
        }
        var mayProxy = /^(?!\/(index\.html$|.*\.hot-update\.json$|sockjs-node\/)).*$/;
        devServer.use(mayProxy, httpProxyMiddleware(pathname => mayProxy.test(pathname), {
            target: proxy,
            logLevel: 'silent',
            onError: onProxyError(proxy),
            secure: false,
            changeOrigin: true
        }));
    }
    devServer.use(devServer.middleware);
}
function runDevServer(port, protocol) {
    var devServer = new WebpackDevServer(compiler, {
        contentBase: path.join(__dirname, 'public/'),
        hot: true,
        historyApiFallback: true,
        publicPath: config.output.publicPath,
        quiet: true,
        watchOptions: {
            ignored: /node_modules/
        },
        https: protocol === "https" ? true : false
    });
    devServer.listen(port, (err, result) => {
        if (err) {
            return console.log(err);
        }
        console.log(chalk.cyan('Starting the development server...'));
        console.log();
    });
}
function run(port) {
    var protocol = process.env.HTTPS === 'true' ? "https" : "http";
    setupCompiler(port, protocol);
    runDevServer(port, protocol);
}
run(port);
//# sourceMappingURL=server.js.map