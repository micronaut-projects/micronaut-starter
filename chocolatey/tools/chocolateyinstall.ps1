$version = '2.1.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '68ABCCEA80DF0B8BC3D9780736192610B198AE9BCC05753687AF69D0D3DF9617'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
