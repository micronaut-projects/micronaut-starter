// App.js
import React from 'react';

function App({name, url}) {
    return (
        <html>
        <head>
            <title>Hello World!</title>
            <meta charSet="UTF-8"/>
        </head>
        <body>
            <p>Hello there {name}, I'm saying hi from SSR React!</p>
            <p>URL is {url}</p>
        </body>
        </html>
    );
}

export default App;
