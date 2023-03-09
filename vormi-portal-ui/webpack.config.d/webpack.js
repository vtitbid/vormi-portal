config.resolve.modules.push("../../processedResources/js/main");

if (config.devServer) {
    config.devServer.hot = true;

   config.devServer.historyApiFallback = {
       index: "index.html"
   }

    config.devtool = 'eval-cheap-source-map';
} else {
    config.devtool = undefined;
}

// disable bundle size warning
config.performance = {
    assetFilter: function (assetFilename) {
      return !assetFilename.endsWith('.js');
    },
};