import React from 'react';
import {hydrateRoot} from 'react-dom/client';

const pageComponentName = Micronaut.rootComponent;

import(`./components/${pageComponentName}.js`).then(module => {
    const PageComponent = module[pageComponentName]
    hydrateRoot(document, <PageComponent {...Micronaut.rootProps}/>)
})
