import React from "react";
import {useCurrentUser} from "../utils/Authn";

function Profile() {
    const currentUser = useCurrentUser()
    return (
        <div>
            <h1>{`Hello ${currentUser}`}</h1>
        </div>
    );
}

export default Profile;