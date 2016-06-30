(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .controller('OrderDetailsDialogController', OrderDetailsDialogController);

    OrderDetailsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'OrderDetails', 'StoreOrder', 'Product'];

    function OrderDetailsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, OrderDetails, StoreOrder, Product) {
        var vm = this;

        vm.orderDetails = entity;
        vm.clear = clear;
        vm.save = save;
        vm.storeorders = StoreOrder.query();
        vm.products = Product.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.orderDetails.id !== null) {
                OrderDetails.update(vm.orderDetails, onSaveSuccess, onSaveError);
            } else {
                OrderDetails.save(vm.orderDetails, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('computerWebstoreApp:orderDetailsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
