export type State =
    | { tag: 'editing'; error?: string; inputs: { username: string; email: string, password: string } }
    | { tag: 'submitting'; username: string }
    | { tag: 'redirect' };

export type Action =
    | { type: 'edit'; inputName: string; inputValue: string }
    | { type: 'submit' }
    | { type: 'error'; message: string }
    | { type: 'success' };

function logUnexpectedAction(state: State, action: Action) {
    console.log(`Unexpected action '${action.type} on state '${state.tag}'`);
}

export default function reduce(state: State, action: Action): State {
    switch (state.tag) {
        case 'editing':
            if (action.type === 'edit') {
                return {
                    tag: 'editing',
                    error: undefined,
                    inputs: {...state.inputs, [action.inputName]: action.inputValue}
                };
            } else if (action.type === 'submit') {
                return {tag: 'submitting', username: state.inputs.username};
            } else {
                logUnexpectedAction(state, action);
                return state;
            }

        case 'submitting':
            if (action.type === 'success') {
                return {tag: 'redirect'};
            } else if (action.type === 'error') {
                return {tag: 'editing', error: action.message, inputs: {username: state.username, email: '', password: ''}};
            } else {
                logUnexpectedAction(state, action);
                return state;
            }

        case 'redirect':
            logUnexpectedAction(state, action);
            return state;
    }
}