(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('product-details', {
            parent: 'entity',
            url: '/product-details',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProductDetails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product-details/product-details.html',
                    controller: 'ProductDetailsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('product-details-detail', {
            parent: 'entity',
            url: '/product-details/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProductDetails'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product-details/product-details-detail.html',
                    controller: 'ProductDetailsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ProductDetails', function($stateParams, ProductDetails) {
                    return ProductDetails.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('product-details.new', {
            parent: 'product-details',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-details/product-details-dialog.html',
                    controller: 'ProductDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                details: null,
                                attributes: null,
                                productionYear: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('product-details', null, { reload: true });
                }, function() {
                    $state.go('product-details');
                });
            }]
        })
        .state('product-details.edit', {
            parent: 'product-details',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-details/product-details-dialog.html',
                    controller: 'ProductDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProductDetails', function(ProductDetails) {
                            return ProductDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('product-details', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('product-details.delete', {
            parent: 'product-details',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-details/product-details-delete-dialog.html',
                    controller: 'ProductDetailsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProductDetails', function(ProductDetails) {
                            return ProductDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('product-details', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
