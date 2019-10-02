package com.example.vittles.di

import javax.inject.Scope

/**
 * Scope used for application feature components. This scope is needed for the Dagger framework,
 * otherwise it cannot generate the needed builds and will conflict the build.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 */
@Scope
annotation class ApplicationScope