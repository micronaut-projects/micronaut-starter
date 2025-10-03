$version = '4.9.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '1B1E29340774975ED5ABD7641C4EFEBDEC8A17AB0B9E82C6F8ACC776F01D32E7'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
