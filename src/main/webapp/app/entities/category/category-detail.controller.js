(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .controller('CategoryDetailController', CategoryDetailController);

    CategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Category', 'Product'];

    function CategoryDetailController($scope, $rootScope, $stateParams, entity, Category, Product) {
        var vm = this;

        vm.category = entity;

        var unsubscribe = $rootScope.$on('computerWebstoreApp:categoryUpdate', function(event, result) {
            vm.category = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
