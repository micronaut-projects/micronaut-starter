$version = '3.9.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '952599D6B09BCFFD17C4B5E47887EBDC46F506560946E4D8A12C10DB7A75C85E'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
