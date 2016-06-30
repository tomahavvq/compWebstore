(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .controller('StoreOrderDetailController', StoreOrderDetailController);

    StoreOrderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'StoreOrder', 'User', 'OrderDetails'];

    function StoreOrderDetailController($scope, $rootScope, $stateParams, entity, StoreOrder, User, OrderDetails) {
        var vm = this;

        vm.storeOrder = entity;

        var unsubscribe = $rootScope.$on('computerWebstoreApp:storeOrderUpdate', function(event, result) {
            vm.storeOrder = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
