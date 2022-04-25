function HelloWorld({ name }) {
    return React.createElement('div', null, `Hello there, ${name}`);
}

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(React.createElement(HelloWorld, { name: 'Thomas' }, null));