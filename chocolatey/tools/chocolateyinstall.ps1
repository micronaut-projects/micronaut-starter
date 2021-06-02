$version = '3.0.0-M1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'B0D1F9F61F9D7D1E48FD07CC0610DC028F67318FE2688F0FCB6DB9FE0571F5C5'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
