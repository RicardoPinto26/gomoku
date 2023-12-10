import React, {createContext, useContext, useState,} from 'react'

type UserManager = {
    user: string | undefined,
    setUser: (user: string) => void
    clearUser : () => void
}
const LoggedInContext = createContext<UserManager>({
    user: undefined,
    setUser: () => {
    },
    clearUser: () => {
    },
})

export function AuthnContainer({children}: { children: React.ReactNode }) {
    const [user, setUser] = useState<string | undefined>(undefined)
    console.log(`AuthnContainer: ${user}`)
    return (
        <LoggedInContext.Provider value={
            {
                user: user,
                setUser: setUser,
                clearUser: () => {
                    setUser(undefined)
                }
            }
        }>
            {children}
        </LoggedInContext.Provider>
    )
}

export function useCurrentUser() {
    return useContext(LoggedInContext).user
}

export function useSetUser() {
    return useContext(LoggedInContext).setUser
}

export function useUserManager() {
    return useContext(LoggedInContext)
}