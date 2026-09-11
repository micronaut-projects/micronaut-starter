$version = '4.10.18'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'C744A0BCC59A79D6114D76038E6FD5E78CA32C4B0D8059E93021150E9BED1D9B'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
