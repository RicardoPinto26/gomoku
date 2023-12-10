import React, {createContext, useContext, useEffect, useState} from "react";

export type MatchmakingSettings = {
    gridSize: number;
    winningLength: number;
    opening: string;
    pointsMargin: number;
    overflow: boolean;
};

const defaultSettings: MatchmakingSettings = {
    gridSize: 15,
    winningLength: 5,
    opening: "FREESTYLE",
    pointsMargin: 200,
    overflow: true,
};

const MatchmakingSettingsContext = createContext<{
    settings: MatchmakingSettings;
    setSettings: React.Dispatch<React.SetStateAction<MatchmakingSettings>>;
}>({
    settings: defaultSettings,
    setSettings: () => {
    },
});

export const useMatchmakingSettings = () => useContext(MatchmakingSettingsContext);


export function MatchmakingSettingsProvider({children}: { children: React.ReactNode }) {
    const [settings, setSettings] = useState<MatchmakingSettings>(() => {
        const localData = localStorage.getItem('matchmakingSettings')
        return localData ? JSON.parse(localData) : defaultSettings
    });

    useEffect(() => {
        localStorage.setItem('matchmakingSettings', JSON.stringify(settings))
    }, [settings])

    return (
        <MatchmakingSettingsContext.Provider value={{ settings, setSettings }}>
            {children}
        </MatchmakingSettingsContext.Provider>
    );
}
