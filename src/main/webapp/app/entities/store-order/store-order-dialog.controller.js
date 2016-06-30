(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .controller('StoreOrderDialogController', StoreOrderDialogController);

    StoreOrderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StoreOrder', 'User', 'OrderDetails'];

    function StoreOrderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StoreOrder, User, OrderDetails) {
        var vm = this;

        vm.storeOrder = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.orderdetails = OrderDetails.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.storeOrder.id !== null) {
                StoreOrder.update(vm.storeOrder, onSaveSuccess, onSaveError);
            } else {
                StoreOrder.save(vm.storeOrder, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('computerWebstoreApp:storeOrderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
