/**
 *  Virtual table
 *   - lookup table for dynamic/late binding virtual functions
 *   - created by compiler
 *   - every class (derived or not) with virtual methods gets a vtable
 *   - vtable contains N entries. N = number of virtual functions.
 *   - vtable is a static array
 *   - entries are: 
 *         1. function pointer pointing to the most-derived func
 *         2. pointer to the base class vtable
 *
 */
